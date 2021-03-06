package cn.edu.hbpu.edu.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;

import cn.edu.hbpu.edu.bean.ClassInfo;
import cn.edu.hbpu.edu.bean.Course;
import cn.edu.hbpu.edu.bean.Curriculum;
import cn.edu.hbpu.edu.bean.PageBean;
import cn.edu.hbpu.edu.dao.ClassInfoDAO;
import cn.edu.hbpu.edu.dao.CourseDAO;
import cn.edu.hbpu.edu.dao.CurriculumDAO;
import cn.edu.hbpu.edu.factory.EduFactory;
import cn.edu.hbpu.edu.service.PaginationService;
import cn.edu.hbpu.edu.util.BaseServlet;
import cn.edu.hbpu.edu.util.MyDateConverter;

import com.alibaba.fastjson.JSON;

/**
 * 
 * @ClassName: TeacherServlet
 * @Description: teacher控制器
 * @author ganquanzhong
 * @date 2018年4月26日 下午5:59:55
 */
public class CurriculumServlet extends BaseServlet {

	/**
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	*/
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @Title: findTeacherByPage
	 * @Description: 根据条件查询Sataff，并且分页显示
	 * @author ganquanzhong
	 * @date 2018年4月25日 下午7:22:47
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String findCurriculumByPage(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		// 从前端页面获取数据
		String cp = request.getParameter("cp");
		String classname = request.getParameter("classname");
		String collegename = request.getParameter("collegename");

		// System.out.println("query"+teachername+tsubjectname);

		// 封装查询条件到list中
		List<String> list = new ArrayList<String>();
		list.add(classname);
		list.add(collegename);

		int currentPage = cp == null ? 1 : Integer.parseInt(cp);

		// 创建Staff实体类分页对象
		PageBean<Curriculum> page = new PageBean<Curriculum>();

		page.setCurrentPage(currentPage);

		page.setPageSize(6);

		PaginationService<Curriculum> service = new PaginationService<Curriculum>(EduFactory.getCurriculumDAOInstance());

		// 分页
		page = service.paging(page, list);
		// 将pageBean对象转化为json数据
		String json = JSON.toJSONString(page);

		try {
			PrintWriter out = response.getWriter();
			out.print(json);

			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// System.out.println("----teacher列表！---");
		return "";
	}

//	// 根据指定的id查询Staff
//	public String findTeacherById(HttpServletRequest request,
//			HttpServletResponse response) {
//
//		// 获取参数
//		String id = request.getParameter("id");
//
//		TeacherDAO teacherDao = EduFactory.getTeacherDAOInstance();
//
//		// 调用StaffDAO，返回id对应的StaffBean
//		Teacher teacher = teacherDao.findById(Long.parseLong(id));
//
//		// 保存到request对象中
//		request.setAttribute("teacher", teacher);
//
//		return "teac-info.jsp";
//	}

	
	public String preAddCurriculum(HttpServletRequest request,	HttpServletResponse response) {
		
		ClassInfoDAO classInfoDAO=EduFactory.getClassInfoDAOInstance();
		
		CourseDAO courseDAO=EduFactory.getCourseDAOInstance();
		
		List<ClassInfo> classList = classInfoDAO.getListByStatus();
		List<Course> courseList = courseDAO.getCourseList();
		
		request.setAttribute("classList", classList);
		request.setAttribute("courseList", courseList);
		
		return "curriculum-add.jsp";
	}
	
	// 添加课程
	public String addCurriculum(HttpServletRequest request,	HttpServletResponse response) {
		// 将form参数 自动封装 Person对象
		Curriculum curriculum = new Curriculum();

		// 将String类型 转换 java.util.Date类型 --- 自定义转换器
		// 在封装数据之前 ，注册转换器
		ConvertUtils.register(new MyDateConverter(), Date.class);

		try {
			BeanUtils.populate(curriculum, request.getParameterMap());
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		CurriculumDAO curriculumDAO = EduFactory.getCurriculumDAOInstance();
		
		int result = curriculumDAO.add(curriculum);

		try {
			PrintWriter out = response.getWriter();
			if (result != 0) {
				out.print(1);
			} else {
				out.print(-1);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "";
	}
	
	
//
//	// 根据指定的id查询Staff
//	public String preEditTeacherById(HttpServletRequest request,
//			HttpServletResponse response) {
//
//		// 获取参数
//		String id = request.getParameter("id");
//
//		TeacherDAO teacherDao = EduFactory.getTeacherDAOInstance();
//
//		// 调用StaffDAO，返回id对应的StaffBean
//		Teacher teacher = teacherDao.findById(Long.parseLong(id));
//
//		// 保存到request对象中
//		request.setAttribute("teacher", teacher);
//
//		return "teac-edit.jsp";
//	}
//
//	// 编辑
//	public String editTeacherById(HttpServletRequest request,
//			HttpServletResponse response) {
//		// 将form参数 自动封装 Person对象
//		Teacher teacher = new Teacher();
//
//		// 将String类型 转换 java.util.Date类型 --- 自定义转换器
//		// 在封装数据之前 ，注册转换器
//		ConvertUtils.register(new MyDateConverter(), Date.class);
//
//		try {
//			BeanUtils.populate(teacher, request.getParameterMap());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
////		System.out.println(teacher);
//		
//		TeacherDAO teacherDAO = EduFactory.getTeacherDAOInstance();
//		int result = teacherDAO.update(teacher);
//
//		try {
//			PrintWriter out = response.getWriter();
//			if (result != 0) {
//				out.print(1);
//			} else {
//				out.print(-1);
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return "";
//	}
//
//	// 删除
//	public void deleteById(HttpServletRequest request,
//			HttpServletResponse response) {
//		// 获取参数
//		String id = request.getParameter("id");
//		// System.err.println("删除 " + id);
//		TeacherDAO dao = EduFactory.getTeacherDAOInstance();
//
//		// 调用StaffDAO，返回id对应的StaffBean
//		int i = dao.delete(Long.parseLong(id));
//
//		try {
//			PrintWriter out = response.getWriter();
//			if (i == 0) {
//				out.print("删除失败！");
//			} else {
//				out.print("删除成功！");
//			}
//			out.flush();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

}
